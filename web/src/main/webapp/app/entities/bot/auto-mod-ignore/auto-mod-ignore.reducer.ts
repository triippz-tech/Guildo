import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { IAutoModIgnore, defaultValue } from 'app/shared/model/bot/auto-mod-ignore.model';

export const ACTION_TYPES = {
  FETCH_AUTOMODIGNORE_LIST: 'autoModIgnore/FETCH_AUTOMODIGNORE_LIST',
  FETCH_AUTOMODIGNORE: 'autoModIgnore/FETCH_AUTOMODIGNORE',
  CREATE_AUTOMODIGNORE: 'autoModIgnore/CREATE_AUTOMODIGNORE',
  UPDATE_AUTOMODIGNORE: 'autoModIgnore/UPDATE_AUTOMODIGNORE',
  DELETE_AUTOMODIGNORE: 'autoModIgnore/DELETE_AUTOMODIGNORE',
  RESET: 'autoModIgnore/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAutoModIgnore>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type AutoModIgnoreState = Readonly<typeof initialState>;

// Reducer

export default (state: AutoModIgnoreState = initialState, action): AutoModIgnoreState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_AUTOMODIGNORE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_AUTOMODIGNORE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_AUTOMODIGNORE):
    case REQUEST(ACTION_TYPES.UPDATE_AUTOMODIGNORE):
    case REQUEST(ACTION_TYPES.DELETE_AUTOMODIGNORE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_AUTOMODIGNORE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_AUTOMODIGNORE):
    case FAILURE(ACTION_TYPES.CREATE_AUTOMODIGNORE):
    case FAILURE(ACTION_TYPES.UPDATE_AUTOMODIGNORE):
    case FAILURE(ACTION_TYPES.DELETE_AUTOMODIGNORE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_AUTOMODIGNORE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_AUTOMODIGNORE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_AUTOMODIGNORE):
    case SUCCESS(ACTION_TYPES.UPDATE_AUTOMODIGNORE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_AUTOMODIGNORE):
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

const apiUrl = 'services/bot/api/auto-mod-ignores';

// Actions

export const getEntities: ICrudGetAllAction<IAutoModIgnore> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_AUTOMODIGNORE_LIST,
  payload: axios.get<IAutoModIgnore>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IAutoModIgnore> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_AUTOMODIGNORE,
    payload: axios.get<IAutoModIgnore>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IAutoModIgnore> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_AUTOMODIGNORE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAutoModIgnore> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_AUTOMODIGNORE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAutoModIgnore> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_AUTOMODIGNORE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
