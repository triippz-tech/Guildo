import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { IRootPage, defaultValue } from 'app/shared/model/blog/root-page.model';

export const ACTION_TYPES = {
  FETCH_ROOTPAGE_LIST: 'rootPage/FETCH_ROOTPAGE_LIST',
  FETCH_ROOTPAGE: 'rootPage/FETCH_ROOTPAGE',
  CREATE_ROOTPAGE: 'rootPage/CREATE_ROOTPAGE',
  UPDATE_ROOTPAGE: 'rootPage/UPDATE_ROOTPAGE',
  DELETE_ROOTPAGE: 'rootPage/DELETE_ROOTPAGE',
  RESET: 'rootPage/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRootPage>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type RootPageState = Readonly<typeof initialState>;

// Reducer

export default (state: RootPageState = initialState, action): RootPageState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ROOTPAGE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ROOTPAGE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ROOTPAGE):
    case REQUEST(ACTION_TYPES.UPDATE_ROOTPAGE):
    case REQUEST(ACTION_TYPES.DELETE_ROOTPAGE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_ROOTPAGE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ROOTPAGE):
    case FAILURE(ACTION_TYPES.CREATE_ROOTPAGE):
    case FAILURE(ACTION_TYPES.UPDATE_ROOTPAGE):
    case FAILURE(ACTION_TYPES.DELETE_ROOTPAGE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_ROOTPAGE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ROOTPAGE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ROOTPAGE):
    case SUCCESS(ACTION_TYPES.UPDATE_ROOTPAGE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ROOTPAGE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'services/blog/api/root-pages';

// Actions

export const getEntities: ICrudGetAllAction<IRootPage> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_ROOTPAGE_LIST,
  payload: axios.get<IRootPage>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IRootPage> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ROOTPAGE,
    payload: axios.get<IRootPage>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IRootPage> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ROOTPAGE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRootPage> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ROOTPAGE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRootPage> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ROOTPAGE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
