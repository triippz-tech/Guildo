import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { IGuildServerProfile, defaultValue } from 'app/shared/model/bot/guild-server-profile.model';

export const ACTION_TYPES = {
  FETCH_GUILDSERVERPROFILE_LIST: 'guildServerProfile/FETCH_GUILDSERVERPROFILE_LIST',
  FETCH_GUILDSERVERPROFILE: 'guildServerProfile/FETCH_GUILDSERVERPROFILE',
  CREATE_GUILDSERVERPROFILE: 'guildServerProfile/CREATE_GUILDSERVERPROFILE',
  UPDATE_GUILDSERVERPROFILE: 'guildServerProfile/UPDATE_GUILDSERVERPROFILE',
  DELETE_GUILDSERVERPROFILE: 'guildServerProfile/DELETE_GUILDSERVERPROFILE',
  RESET: 'guildServerProfile/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IGuildServerProfile>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type GuildServerProfileState = Readonly<typeof initialState>;

// Reducer

export default (state: GuildServerProfileState = initialState, action): GuildServerProfileState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_GUILDSERVERPROFILE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_GUILDSERVERPROFILE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_GUILDSERVERPROFILE):
    case REQUEST(ACTION_TYPES.UPDATE_GUILDSERVERPROFILE):
    case REQUEST(ACTION_TYPES.DELETE_GUILDSERVERPROFILE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_GUILDSERVERPROFILE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_GUILDSERVERPROFILE):
    case FAILURE(ACTION_TYPES.CREATE_GUILDSERVERPROFILE):
    case FAILURE(ACTION_TYPES.UPDATE_GUILDSERVERPROFILE):
    case FAILURE(ACTION_TYPES.DELETE_GUILDSERVERPROFILE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_GUILDSERVERPROFILE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_GUILDSERVERPROFILE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_GUILDSERVERPROFILE):
    case SUCCESS(ACTION_TYPES.UPDATE_GUILDSERVERPROFILE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_GUILDSERVERPROFILE):
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

const apiUrl = 'services/bot/api/guild-server-profiles';

// Actions

export const getEntities: ICrudGetAllAction<IGuildServerProfile> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_GUILDSERVERPROFILE_LIST,
  payload: axios.get<IGuildServerProfile>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IGuildServerProfile> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_GUILDSERVERPROFILE,
    payload: axios.get<IGuildServerProfile>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IGuildServerProfile> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_GUILDSERVERPROFILE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IGuildServerProfile> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_GUILDSERVERPROFILE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IGuildServerProfile> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_GUILDSERVERPROFILE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
